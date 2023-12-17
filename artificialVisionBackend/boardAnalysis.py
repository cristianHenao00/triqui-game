import numpy as np
from collections import Counter
import figureDetection as Dt
import cv2
import json
import requests


def getMoreRepetitiveHierarchy(contours, hierarchy):
    jerarquias = []
    for i, contour in enumerate(contours):
        jerarquias.append(hierarchy[0][i][3])
    report = Counter(jerarquias)
    # Encuentra el número que más se repite y cuántas veces
    number, repetitions = report.most_common(1)[0]
    return number, repetitions


def imageProcess(image):
    # Convertir el fotograma a escala de grises
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    # Aplicar un filtro Gaussiano para reducir el ruido
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    # Detectar bordes con Canny
    edges = cv2.Canny(blurred, threshold1=100, threshold2=200)
    kernel = np.ones((5, 5), np.uint8)
    # Realizar una dilatación a la zona blanca (bordes)
    edges = cv2.dilate(edges, kernel, iterations=1)
    # Encontrar los contornos en la imagen con información de jerarquía
    contours, hierarchy = cv2.findContours(
        edges, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    if contours:
        hierarchyNumber, repetitions = getMoreRepetitiveHierarchy(
            contours, hierarchy)
        # Filtrar los contornos para obtener los cuadrados internos
        inner_squares = []
        counter = 0
        for i, contour in enumerate(contours):
            if hierarchy[0][i][3] == hierarchyNumber and cv2.contourArea(contour) > 1000:
                counter += 1
                inner_squares.append(contour)
                x, y, w, h = cv2.boundingRect(contour)
                square = frame[y + 10:y + h - 10, x + 10:x + w - 10]
                if square.size != 0:  # Verifica si el recorte no está vacío
                    cv2.imwrite(f'cuts/inner_square_{counter}.jpg', square)
                else:
                    pass
        # Dibujar los contornos en el fotograma original (en color)
        # Dibuja los contornos internos en verde
        cv2.drawContours(image, inner_squares, -1, (0, 255, 0), 3)
        return image, edges
    else:
        return image, edges


def createBoard():
    board = []
    column = 2
    row = ["", "", ""]
    for i in range(9, -1, -1):
        if i % 3 == 0 and i != 9:
            board.append(row.copy())
            column = 2
            if i == 0:
                break
        img = cv2.imread(f"cuts/inner_square_{i}.jpg")
        img = cv2.resize(img, (400, 400))
        type = Dt.detectarFiguraTablero(img)
        row[column] = type
        column -= 1
    return board


def postJson():
    board = createBoard()
    print(board[0])
    print(board[1])
    print(board[2])
    print("-------------------------")
    mensaage = {"board": board}
    menssageJson = json.dumps(mensaage)
    # print(menssageJson)
    url = "http://localhost:8081"
    headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
    r = requests.post(url, data=menssageJson, headers=headers)
    if r.status_code == 200:
        print("Mensaje enviado correctamente")
    else:
        print("Error al enviar el mensaje")


# Inicializar la captura de video desde la cámara 1 (puedes ajustar el número de cámara según tu configuración)
cap = cv2.VideoCapture("http://192.168.43.61:4747/video")


while True:
    # Capturar un fotograma de la cámara
    ret, frame = cap.read()
    frame, edges = imageProcess(frame)
    cv2.imshow('edges', edges)
    cv2.imshow('Image with Inner Contours', frame)
    if cv2.waitKey(1) & 0xFF == ord('e'):
        postJson()
    if cv2.waitKey(2) & 0xFF == ord('q'):
        break
# Liberar la captura de video y cerrar todas las ventanas
cap.release()
cv2.destroyAllWindows()
