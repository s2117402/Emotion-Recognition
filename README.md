# Breif Introduction
This is a basic structure I did for my AI class.The ideal is divided our whole system to 2 parts.
One is mobile terminal,the other is server terminal.

# Mobile terminal
Mobile terminal is an android app we use it 
to take human face,we use OpenCV library to achive this,detect human face and take it as a picture 
then process it to a 100*100 picture.The next step we have to attach this picture to the post 
function and send it to server.Here is the tutorial below about opencv for Java I used.

http://docs.opencv.org/2.4/doc/tutorials/introduction/desktop_java/java_dev_intro.html

#Server terminal
I use python to finish this part.At first of all,I use python based on keras(tensorflow) to train
a model,we input a 100*100 human face picture,it will give you a output with 3 possible cases.When 
we were trainning model,we used three convolution layer to process the pictures for a higher accuracy,
the datasets we used is collected by ourselves,we catogoried it ourselves,we get a around 93% accuracy
evetually.Then we used python based flask to build a web server to receive the post from mobile terminal,
after getting the picture,it will input that picture to the model and return back the output the model 
predict.

