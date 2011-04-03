kfuzzy: kfuzzy.o
	g++ -o kfuzzy kfuzzy.o -L/usr/local/lib -lgflags -lgtest

kfuzzy.o: kfuzzy.cc
	g++ -DHOME -g -o kfuzzy.o -c kfuzzy.cc

clear:
	rm kfuzzy.o
