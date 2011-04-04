kfuzzy: kfuzzy.o vector.o
	g++ -o kfuzzy kfuzzy.o vector.o -L/usr/local/lib -lgflags -lgtest

tests: kfuzzy
	./kfuzzy --run_all_tests

vector.o: math/vector.h math/vector.cc
	g++ -DHOME -g -o vector.o -c math/vector.cc -Imath/

kfuzzy.o: kfuzzy.cc math/vector.h algo/strategy.hpp
	g++ -DHOME -g -o kfuzzy.o -c kfuzzy.cc -I.

clear:
	rm kfuzzy.o
	rm vector.o
