import re

f = open("Log.txt", "r")
mensaje = f.read()
print("\n",mensaje)
f.close()

EXPRESION = mensaje

transiciones = EXPRESION.split()
contador = {"T0": 0, "T1": 0, "T2": 0, "T3": 0, "T4": 0,
            "T5": 0, "T6": 0, "T7": 0, "T8": 0, "T9": 0, "T10": 0}

for transicion in transiciones:
    contador[transicion] += 1

patron = r"((T0 )(.*?)((T1 )(.*?)(T10 )|(T2 )(.*?)(T3 )(.*?)(T4 )(.*?)(T5 ))|(T6 )(.*?)(T7 )(.*?)(T8 )(.*?)(T9 ))(.*?)"
resultado = EXPRESION
condicion = True
print("\n",contador)
print("\ninvariante 1:", contador["T10"], "\ninvariante 2:", contador["T5"], "\ninvariante 3:", contador["T9"])

while condicion:
    condicion = re.search(patron, resultado)
    resultado = re.sub(
        patron, '\g<3>\g<6>\g<9>\g<11>\g<13>\g<16>\g<18>\g<20>\g<22>', resultado)

if resultado.count != 0:
    print("\nSobrante:\n", resultado)
