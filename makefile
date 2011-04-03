kfuzzy: kfuzzy.o
	g++ -o kfuzzy kfuzzy.o -L/usr/local/lib -lgflags -lgtest

tests: kfuzzy
	./kfuzzy --run_all_tests

kfuzzy.o: kfuzzy.cc math/vector.hpp
	g++ -DHOME -g -o kfuzzy.o -c kfuzzy.cc

clear:
	rm kfuzzy.o
