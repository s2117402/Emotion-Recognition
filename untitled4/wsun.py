from flask import Flask, request
from additive import jiafa
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'hello world'

@app.route('/register', methods=['POST'])
def register():
    a=jiafa(int(request.form['name']),3)
    print request.headers
    print request.form
    print request.form['name']
    print request.form.get('name')
    print request.form.getlist('name')
    print request.form.get('nickname', default='little apple')
    print "result:"+a
    return a

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)