from flask import Flask, request, jsonify
import pyttsx3

app = Flask(__name__)


@app.route('/voice', methods=['POST'])
def audio():
    data = request.get_json()
    if 'message' not in data:
        return jsonify({'message': 'No message found'}), 400

    message = data['message']

    engine = pyttsx3.init()
    voices = engine.getProperty('voices')
    engine.setProperty('voice', voices[0].id)

    engine.setProperty('rate', 150)
    engine.setProperty('volume', 0.9)

    engine.say(message)
    engine.runAndWait()
    engine.stop()

    return jsonify({'message': 'Message received'})


if __name__ == '__main__':
    app.run(debug=True, host='localhost', port=5000)
