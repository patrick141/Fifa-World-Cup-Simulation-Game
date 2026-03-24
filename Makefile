SRC = src
OUT = out

.PHONY: run auto compile clean

run: compile
	java -cp $(OUT) Driver

auto: compile
	java -cp $(OUT) Driver --auto

compile:
	mkdir -p $(OUT)
	javac -d $(OUT) $(SRC)/*.java

clean:
	rm -rf $(OUT)
