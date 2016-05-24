# encoding=utf8  
import csv
from bokeh._legacy_charts import HeatMap, output_file, show
from bokeh.palettes import YlOrRd9 as palette
# from bokeh.plotting import figure, show, output_file
# from bokeh.charts import HeatMap, output_file, show
from math import pi
# from bokeh.models import HoverTool
# from bokeh.plotting import ColumnDataSource, figure, show, output_file
# from bokeh.sampledata.unemployment1948 import data
import time
import pandas as pd
import numpy as np
from numpy.random import random
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
#from pyspark.mllib.clustering import KMeans, KMeansModel
from sklearn.cluster import KMeans
import stopword
import emotion
# from sklearn.manifold import MDS
import matplotlib.pyplot as plt
import matplotlib as mpl
from sklearn import datasets


# from sklearn.decomposition import PCA
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis

t0 = time.time()
aa=0
tweets = []
csvfile = open('/Users/mark19891107/Desktop/Big-Data-Analytics/DataSet/csv/database.csv', 'rb') # 1
for row in csv.reader(csvfile, delimiter=';'): # 2
    a = stopword.preprocess(row[0])
    b = emotion.preprocess(a)
    tweets.append(b)
    aa=aa+1
    if aa > 2000:
    	# print aa
    	break
t1 = time.time()
print aa
print ("done in %fs" % (t1 - t0))
# print tweets

k = 10
# print("Extracting features from the training dataset using a sparse vectorizer")
t0 = time.time()
vectorizer = TfidfVectorizer(max_df=0.5, max_features=10000,min_df=2, stop_words='english', use_idf=True)
X = vectorizer.fit_transform(tweets)
# print vectorizer.get_feature_names()
# print X.toarray()

t1 = time.time()
print("done in %fs" % (t1 - t0))
print("n_samples: %d, n_features: %d" % X.shape)

km = KMeans(n_clusters=k, init='k-means++', max_iter=100, n_init=1, verbose=1)
# print("Clustering sparse data with %s" % km)
t0 = time.time()
km.fit(X)
clusters = km.labels_.tolist()
# print clusters
sort=[]
for i in range(0,k,+1):
    for j in range(0,len(clusters),+1):
        if(clusters[j]==i):
            sort.append(X.toarray()[j])
            # print X.toarray()[j]
t1 = time.time()
print("done in %0.3fs" % (t1 - t0))
KMeans(copy_x=True,init='k-means++',max_iter=100,n_clusters=k,n_init=1,n_jobs=1,precompute_distances="auto",random_state=None,tol=0.0001,verbose=1)
# print("Top terms per cluster:")
order_centroids = km.cluster_centers_.argsort()[:, ::-1]
terms = vectorizer.get_feature_names()
# for i in range(7):
	# print("Cluster %d:" % i)
	# for ind in order_centroids[i, :20]:
		# print(' %s' % terms[ind])

# print(df01)
# print(len(df01))
# print()

# groups = df.groupby('label')
# groups01 = df01.groupby('label')

# fig, ax = plt.subplots(figsize=(17, 10))
# ax.margins(0.05)

# for name, group in groups01:
#     ax.plot(group.x, group.y, marker='o', linestyle='', ms=12,label="cluster_names[name]", color="cluster_colors[name]",mec='none')
#     ax.set_aspect('auto')
#     ax.tick_params(\
#         axis= 'x',
#         which='both',
#         bottom='off',
#         top='off',
#         labelbottom='off')
#     ax.tick_params(\
#         axis= 'y',
#         which='both',
#         left='off',
#         top='off',
#         labelleft='off')
#     ax.legend(numpoints=1)


# for i in range(ix_start, ix_stop):
#     ax.text(df01.ix[i]['x'], df01.ix[i]['y'], df01.ix[i]['txt'],size=10)
# plt.show()



# pca = PCA(n_components=2)
# X_r = pca.fit(X.toarray()).transform(X.toarray())
# print X_r



print
print
sortT=[]
stemparray=[]
for i in range(0,len(vectorizer.get_feature_names()),+1):
    for j in range(0,len(sort),+1):
        stemparray.append(sort[j][i])
    sortT.append(stemparray)
    stemparray=[]

df = pd.DataFrame(sortT)
output_file('heatmap.html')

hm = HeatMap(df)

show(hm)
# print sortT
# print len(vectorizer.get_feature_names())
# print len(sort)
# print len(sortT)
# (dict, OrderedDict, lists, arrays and DataFrames are valid inputs)
# data = {'terms': vectorizer.get_feature_names(),'terms_count': sortT,'count': [1]*len(sortT)}
# hm = HeatMap(data, x='terms', y='count', values='terms_count',title='homework2', stat=None)
# # data = {'fruit': ['apples']*3 + ['bananas']*3 + ['pears']*3,'fruit_count': [4, 5, 8, 1, 2, 4, 6, 5, 4],'sample': [1, 2, 3]*3}

# # hm = HeatMap(data, x='fruit', y='sample', values='fruit_count',title='Fruits', stat=None)

# output_file('heatmap.html')
# show(hm)

# MDS()
# mds = MDS(n_components=2, dissimilarity="precomputed", random_state=1)
# dist = 1 - cosine_similarity(sortT)
# # print dist
# pos = mds.fit_transform(dist)
# # print pos
# xs, ys = pos[:, 0], pos[:, 1]
# cluster_colors = {0: '#1b9e77', 1: '#d95f02', 2: '#7570b3', 3:'#e7298a', 4: '#66a61e', 5: '#9990b3', 6: '#e8888a'}
# print len(xs)
# print len(ys)
# df = pd.DataFrame(dict(x=xs, y=ys, label=vectorizer.get_feature_names(), txt=sortT))
# ix_start = 0
# ix_stop  = aa
# df01 = df[ix_start:ix_stop]

# xx=[]
# yy=[]
# for i in pos:
#     xx.append(i[0])
#     yy.append(i[1])

# output_file("bokeh.html", title="homework2")

# # create a new plot with a title and axis labels
# p = figure(title="homework2", x_axis_label='trem', y_axis_label='value')
# # p.scatter(xx, yy, marker="circle", size=1,
#               line_color="navy", fill_color="orange", alpha=0.5)
# # add a line renderer with legend and line thickness
# # mscatter(p,xx, yy)

# # show the results
# show(p)