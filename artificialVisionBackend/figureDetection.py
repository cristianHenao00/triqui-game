import cv2
import numpy as np

nameWindow = "Calculadora"

def nothing(x):
    pass

def constructorVentana():
    cv2.namedWindow(nameWindow)
    cv2.createTrackbar("min", nameWindow, 0, 255, nothing)
    cv2.createTrackbar("max", nameWindow, 70, 255, nothing)
    cv2.createTrackbar("kernel", nameWindow, 5, 100, nothing)
    cv2.createTrackbar("areaMin", nameWindow, 500, 10000, nothing)

def calcularAreas(figuras):
    areas = []
    for figuraActual in figuras:
        areas.append(cv2.contourArea(figuraActual))
    return areas

def detectarFigura(imagenOriginal):
    imagenGris = cv2.cvtColor(imagenOriginal, cv2.COLOR_BGR2GRAY)
    cv2.imshow("Gris", imagenGris)
    min = cv2.getTrackbarPos("min", nameWindow)
    max = cv2.getTrackbarPos("max", nameWindow)
    bordes = cv2.Canny(imagenGris, min, max)
    cv2.imshow("Bordes", bordes)
    tamañoKernel = cv2.getTrackbarPos("kernel", nameWindow)
    kernel = np.ones((tamañoKernel, tamañoKernel), np.uint8)
    bordes = cv2.dilate(bordes, kernel)
    cv2.imshow("Bordes Modificado", bordes)
    figuras, jerarquia = cv2.findContours(bordes, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    areas = calcularAreas(figuras)
    i = 0
    areaMin = cv2.getTrackbarPos("areaMin", nameWindow)
    for figuraActual in figuras:
        if areas[i] >= areaMin:
            # Coordenadas vértices
            vertices = cv2.approxPolyDP(figuraActual, 0.05 * cv2.arcLength(figuraActual, True), True)
            if len(vertices) == 3:
                mensaje = "Triangulo"
                cv2.putText(imagenOriginal, mensaje, (10, 70), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)
                cv2.drawContours(imagenOriginal, [figuraActual], 0, (0, 0, 255), 2)
            elif len(vertices) == 4:
                mensaje = "Cuadrado"
                cv2.putText(imagenOriginal, mensaje, (10, 70), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)
                cv2.drawContours(imagenOriginal, [figuraActual], 0, (0, 0, 255), 2)
        i = i + 1
    cv2.imshow("Imagen", imagenOriginal)
    return imagenOriginal

def detectarFiguraTablero(imagenOriginal):
    imagenGris = cv2.cvtColor(imagenOriginal, cv2.COLOR_BGR2GRAY)
    bordes = cv2.Canny(imagenGris, 0, 70)
    kernel = np.ones((5, 5), np.uint8)
    bordes = cv2.dilate(bordes, kernel)
    figuras, jerarquia = cv2.findContours(bordes, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    areas = calcularAreas(figuras)
    i = 0
    areaMin = 500
    for figuraActual in figuras:
        if areas[i] >= areaMin:
            # Coordenadas vértices
            vertices = cv2.approxPolyDP(figuraActual, 0.05 * cv2.arcLength(figuraActual, True), True)
            if len(vertices) == 3:
                return "T"
            elif len(vertices) == 4:
                return "C"
        i = i + 1
    return " "
