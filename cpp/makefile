kfuzzy: kfuzzy.o vector.o
	g++ -o kfuzzy kfuzzy.o vector.o -L/usr/local/lib -lgflags -lgtest

tests: kfuzzy
	./kfuzzy --run_all_tests

vector.o: math/vector.h math/vector.cc
	g++ -DHOME -g -o vector.o -c math/vector.cc -Imath/ -Wall -pedantic

kfuzzy.o: kfuzzy.cc math/vector.h algo/strategy.hpp io/reader.hpp
	g++ -DHOME -g -o kfuzzy.o -c kfuzzy.cc -I. -Wall -pedantic

clear:
	rm kfuzzy.o
	rm vector.o
