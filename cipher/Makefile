CFLAG=-Wall -Werror -std=gnu99 -g
all: vencrypt vdecrypt scrypt sbencrypt sbdecrypt

vencrypt: vencrypt.c
	gcc $(CFLAG) vencrypt.c -o vencrypt

vdecrypt: vdecrypt.c
	gcc $(CFLAG) vdecrypt.c -o vdecrypt

scrypt: scrypt.c
	gcc $(CFLAG) scrypt.c -o scrypt

sbencrypt: sbencrypt.c
	gcc $(CFLAG) sbencrypt.c -o sbencrypt

sbdecrypt: sbdecrypt.c
	gcc $(CFLAG) sbdecrypt.c -o sbdecrypt

clean:
	rm vencrypt vdecrypt scrypt sbencrypt sbdecrypt
