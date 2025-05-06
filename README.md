# POOBKemon Esmeralda

Realizado por **Robinson Steven Nuñez Portela** y **Sebastián Albarracín Silva**

---

## Requisitos

- Tener instalado **Java JDK 24**
    - Si usas IntelliJ, puedes descargarlo desde allí mismo.
- (Opcional) Descargar la fuente **[Press Start 2P](https://font.download/font/press-start-2p)** para una mejor experiencia visual.

---

## 💻 Navegar a la carpeta del proyecto desde CMD

1. Presiona `Win + R`, escribe `cmd` y presiona Enter.
2. Usa `cd` para llegar a la carpeta del proyecto.

### Ejemplo (en mi caso): ☝️🤓📚✏️
```bash
C:\Users\robin> cd Downloads
C:\Users\robin\Downloads> cd JAVA
C:\Users\robin\Downloads\JAVA> cd POOBKemonAN
C:\Users\robin\Downloads\JAVA\POOBKemonAN>
```
## 💻  Para Compilar usa el siguiente comando desde la cmd:
1. ubícate en usando cd
```bash
cd C:\Users\Tu\carpetaDondeGuardaste\POOBKemonAN
```

2. usa el comando javac para compilar:
```bash
C:\Users\Tu\carpetaDondeGuardaste\POOBKemonAN> javac -d out -sourcepath src src/domain/*.java src/test/*.java
```
3. Dale enter y listo

### Ejemplo (en mi caso): ☝️🤓📚✏️
```bash
C:\Users\robin\Downloads\JAVA\POOBKemonAN>
C:\Users\robin\Downloads\JAVA\POOBKemonAN>javac -d out -sourcepath src src/domain/*.java src/test/*.java
```

## 💻  Ejecutamos el juego
1. Luego de compilar 
2. Vas a usar el comando para ejecutar:
```bash
   java -cp "out;resources" presentation.StartPoobKemon
```
3. Dale a enter deberia aparecer la pantalla del juego


### Ejemplo (en mi caso): ☝️🤓📚✏️
```bash
C:\Users\robin\Downloads\JAVA\POOBKemonAN>java -cp "out;resources" presentation.StartPoobKemon
```
