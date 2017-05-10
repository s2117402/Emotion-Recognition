from flask import Flask, request
from werkzeug.utils import secure_filename
import os
import keras
from PIL import Image
import numpy as np
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = 'static/uploads/'
app.config['ALLOWED_EXTENSIONS'] = set(['png', 'jpg', 'jpeg', 'gif'])
emotion=["Happy","Sad","Angry"]
# For a given file, return whether it's an allowed type or not
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS']

@app.route('/')
def hello_world():
    return 'hello world'

@app.route('/upload', methods=['POST'])
def upload():
    upload_file = request.files['image01']
    if upload_file and allowed_file(upload_file.filename):
        filename = secure_filename(upload_file.filename)
        upload_file.save(os.path.join(app.root_path, app.config['UPLOAD_FOLDER'], filename))
        model = keras.models.load_model('model/my_model.h5')
        data = np.empty((1, 100, 100, 3), dtype="float32")
        img = Image.open("static/uploads/image01.jpg")
        arr = np.asarray(img, dtype="float32")
        data[0, :, :, :] = arr.reshape(100, 100, 1)
        result=model.predict_classes(data, batch_size=32, verbose=1)
        print result[0]
        del model
        return emotion[int(result[0])]
    else:
        return 'hello, '+request.form.get('name', 'little apple')+'. failed'

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
