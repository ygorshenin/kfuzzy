kfuzzy: kfuzzy.o
	g++ -o kfuzzy kfuzzy.o

kfuzzy.o: kfuzzy.cc
	g++ -c -o kfuzzy.o kfuzzy.cc

clear:
	rm kfuzzy.o
