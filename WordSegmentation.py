import os
import sys
import nltk
from nltk.corpus import stopwords
from nltk.stem.snowball import SnowballStemmer
from operator import itemgetter, attrgetter

reload(sys)
sys.setdefaultencoding('utf-8')

file_path = "./Reuters/"
file_postfix = ".html"
file_write_name = "Stemmed_Dic"
corres_file_name = "Raw_Stemmed_Dic"
corres_word_doc_file = "Word_Doc_Dic"

stemmed_dictionary = []
stemmer = SnowballStemmer("english")

raw_sents = []
raw_tokens = []
filtered_tokens = []
raw_stemmed_tokens = []
raw_stemmed_corres_tokens = []
result = []
doc_word_list = []
word_doc_list = []
doc_num = 0

for file_index in range(1,21578):
	file_name = file_path+str(file_index)+file_postfix
	if os.path.exists(file_name):
		doc_num += 1
		doc_file = open(file_name,'r')
		content = doc_file.read()
		
		#replace 
		content = content.replace('\\',' ').replace('\t',' ').replace('\r',' ').replace('&',' ').replace('-',' ').replace('>',' ').replace('<',' ').replace('+',' ').replace('/',' ').replace('\'',' ').replace('*',' ').strip().decode('utf-8')

		#tokenize the document
		raw_sents = nltk.sent_tokenize(content)
		for sent in raw_sents:
			raw_tokens.extend(nltk.word_tokenize(sent))

		#lower the extracted tokens
		lower_tokens = [tokens.lower() for tokens in raw_tokens]

		#get the stop_words for english
		stop_words = stopwords.words('english')

		#extend the stop_words so that we can filter as we set
		stop_words.extend([u'%',u'(',u')',u',',u';',u'.','{','}','$','!','\'','.','\"'])

		#get the tokens list after filtering with stop words
		filtered_tokens.extend([w for w in lower_tokens if not w in stop_words])

		#get the stemmed words
		for tok in filtered_tokens:
			raw_stemmed_tokens.append(stemmer.stem(tok))

		#add the corresponding dictionary
		for i in range(len(filtered_tokens)):
			raw_stemmed_corres_tokens.append((filtered_tokens[i],raw_stemmed_tokens[i]))

		#remove the duplicated tokens
		final_stemmed_tokens = list(set(raw_stemmed_tokens))

		#add to the result
		result = result + final_stemmed_tokens

		#add to the corresponding list
		for i in range(len(final_stemmed_tokens)):
			doc_word_list.append((final_stemmed_tokens[i],str(file_index)))
		doc_word_list = sorted(doc_word_list,key = itemgetter(0))

		word_doc_list.extend(doc_word_list)

		#clear the list for the follow
		raw_stemmed_tokens[:] = []
		final_stemmed_tokens[:] = []
		stop_words[:] = []
		raw_sents[:] = []
		raw_tokens[:] = []
		filtered_tokens[:] = []
		doc_word_list[:] = []

		doc_file.close()

result = list(set(result))
result.sort()
raw_stemmed_corres_tokens = list(set(raw_stemmed_corres_tokens))
raw_stemmed_corres_tokens = sorted(raw_stemmed_corres_tokens,key=itemgetter(1))



stemmed_dic = open(file_write_name,'w')
for item in result:
	stemmed_dic.write(item + '\n')
stemmed_dic.close()

raw_stemmed_dic = open(corres_file_name,'w')
for item in raw_stemmed_corres_tokens:
	raw_stemmed_dic.write(item[0]+'\t'+ item[1] +'\n')
raw_stemmed_dic.close()

word_doc_file = open(corres_word_doc_file,'w')
word_doc_file.write(str(doc_num)+'\n')
word_doc_file.write(str(len(word_doc_list))+'\n')
for item in word_doc_list:
	word_doc_file.write(item[0]+'\t'+item[1]+'\n')
word_doc_file.close()
#print word_doc_list