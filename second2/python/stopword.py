# encoding=utf8  
import nltk
from nltk.tokenize import RegexpTokenizer
from nltk.corpus import stopwords

def preprocess(sentence):
    sentence = sentence.lower()
    tokenizer = RegexpTokenizer(r'https?://[^\s]+|[\w]+')
    tokens = tokenizer.tokenize(sentence)
    filtered_words = [w for w in tokens if not w in stopwords.words('english')]
    a = []
    for w in filtered_words:
    	if "http" not in w:
    		a.append(w)
    return " ".join(a)

sentence = "The Panama Papers prove it: we can afford a universal basic income https://t.co/D5IK0TXFrE goddammit bill gates stealing my money again"
# print preprocess(sentence)