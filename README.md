+   Breif Introduction
+   Mobile terminal
+   Server terminal

###Emotion-recognition

#### Breif Introduction

This is a basic structure I did for my AI class.The ideal is divided our whole system to 2 parts. One is mobile terminal,the other is server terminal.

- - -

#### Trainning data
In this part,I get 120 original pictures with 3 emotions(0-happy,1-sad,2-angry),then I a code form Internet to do some
minor changes to one original picture for getting more pictures
and I get around 2000 pictures as datasets finally.
Here is that tutorials link below which I get the code:
[Link](https://keras-cn.readthedocs.io/en/latest/blog/image_classification_using_very_little_data/) 

Here is the CNN code I use to train model and get model(Keras base on tensorflow).

[Link](https://github.com/s2117402/Emotion-recognition/blob/master/model/newtry.py)

Here are two pictures about the accuracy of the model I trained.

(I don't post the pictures datasets here for privacy purpose.)

---

![acc1](https://raw.githubusercontent.com/s2117402/Emotion-recognition/master/Image/acc1.jpg)


---

#####Purple line is the trainning accuracy,green line is the validation accuracy.

![acc2](https://raw.githubusercontent.com/s2117402/Emotion-recognition/master/Image/acc2.jpg)

---
#### Mobile terminal

Mobile terminal is an android app we use it to take human face,we use OpenCV library to achive this,detect human face and take it as a picture then process it to a 100*100 picture.The next step we have to attach this picture to the post function and send it to server.Here is the tutorials below about opencv for Java I used.

[OpenCv turotials](http://docs.opencv.org/2.4/doc/tutorials/introduction/desktop_java/java_dev_intro.html)

######Here are two pictures about the manipulating of mobile application
when the app detect the human face and show a green rectangle box,press 
#####CAMERA 
to take picture.

![mobile1](https://raw.githubusercontent.com/s2117402/Emotion-recognition/master/Image/mobile1.png)

---

Then we go to another activity,if you want to pridict the emotion of this picture display on your screen,press 
#####TEST
then you can get the result right away.

![mobile1](https://raw.githubusercontent.com/s2117402/Emotion-recognition/master/Image/mobile2.png)


***

#### Server terminal

I use python to finish this part.At first of all,I use python based on keras(tensorflow) to train a model,we input a 100*100 human face picture,it will give you a output with 3 possible cases.When we were trainning model,we used three convolution layer to process the pictures for a higher accuracy, the datasets we used is collected by ourselves,we catogoried it ourselves,we get a around 93% accuracy evetually.Then we used python based flask to build a web server to receive the post from mobile terminal, after getting the picture,it will input that picture to the model and return back the output the model predict.

Here is the picture I took when the python project start to run on Amazon server.

---
![server1](https://raw.githubusercontent.com/s2117402/Emotion-recognition/master/Image/server1.png)
---

Here is the picture when project in server receives the picture from mobile app,predict and send back result.

---
![server2](https://raw.githubusercontent.com/s2117402/Emotion-recognition/master/Image/server2.png)
---

######2 means angry and this IP address is the IP address of the phone I use to post server.The picture received will be stored in /upload folder.
- - -
