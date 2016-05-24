# encoding=utf8  
import re 

def preprocess(tweet):
	reducedTweets = []
	#tweet = tweet.decode('utf-8')
	#tweet = tweet.encode('utf-8')
	reducedTweet = re.sub(r'[\x80-\xff]|[\u][0-9]', '', tweet)
	reducedTweets.append(reducedTweet)
	return " ".join(reducedTweets)

# tw ="iPhone 6s for $450 😳 Only on our website ☺️;Authentic Plug"

# print preprocess(tw)
