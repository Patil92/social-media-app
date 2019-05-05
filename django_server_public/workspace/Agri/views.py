
from django.shortcuts import render
from django.http import HttpResponse

# Create your views here.

import numpy as np
from django.http import JsonResponse
from nltk.corpus import words
from nltk.corpus import stopwords
from sklearn.externals import joblib

#import mmap

def checkSpell(sms):
    sms=sms.lower()
    f=False
    g=False
    q=False
    l=[]
    l=sms.split(" ")
    n=l
    m=[word for word in l if not word in set(stopwords.words('english'))]
    file=open('words.txt').read().strip().split()
    
    for i in range(len(m)):
        if l[i] in words.words():
            f=True
        else:
            f=False
            break
    
    for i in range(len(n)):
        if l[i] in words.words():
            q=True
        else:
            q=False
            break
        
    for i in range(len(l)):
        if l[i] in file:
            g=True
        else:
            g=False
            break
        
    res=f or g or q
    
    result={
        "res":res,
        "name":"Patil",
        "sms":l,
    }
    
    return JsonResponse(result)

def index(request):
    
    sms=request.GET["sms"]
    
    #https://agri-django-server-public-patil.c9users.io/Agri/?sms=hello
    return checkSpell(sms) 
   