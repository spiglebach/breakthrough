<h1 align="center">Áttörés</h1>
<h3 align="center">egy táblajáték online játéklehetőséggel</h3>
<table width="100%" align="center">  
	<tr>  
		<th colspan=2 width="100%">Felhasználói útmutató:</th>   
	</tr>  
	<tr align="center">  
		<td colspan=2><a href=#hu_about>A játékról</a></td>  
	</tr>
	<tr align="center">  
		<td colspan=2><a href=#hu_functions>A program funkciói</a></td>  
	</tr>
	<tr align="center" >  
		<td width="50%"><a href=#host>Online játék hostolása</a></td>  
		<td><a href=#join>Csatlakozás online játékhoz</a></td>  
	</tr>
</table>
<table align="center" width="100%">
	<tr>  
		<th>Fejlesztés:</th>  
	</tr> 
	</tr>
	<tr align="center">  
		<td><a href=#known>Ismert problémák</a></td>  
	</tr> 
	<tr align="center">  
		<td><a href=#fixed>Nemrég javított problémák</a></td>  
	</tr>  
	<tr align="center">  
		<td><a href=#planned>Továbbfejlesztési lehetőségek</a></td>  
	</tr>  
</table>
<h3>Felhasználói útmutató</h3>
<h4 id="hu_about">A játékról</h4>
Az Áttörés egy kétfős táblajáték. A játékosok egy mérkőzést két sornyi bábuval kezdik. A piros színű játékos kezd. A kör lépésenként váltakozik, a játékosoknak körönként egy lépése van.

**Egy játékos a bábujával léphet:**

 1. **előre&ast;** vagy **átlósan előre** ha a megcélzott mező **üres** *(forward in the direction of the opponent's home row)*
 2. **átlósan előre** ha a megcélzott mezőn **az ellenfél egy bábuja található**, ebben az esetben **az ellenfél bábuja kiesik a játékból**
&ast;*előre alatt a kezdősorunktól az ellenfél kezdősora felé tartó irányt értjük*

**A játék célja:** elérni saját bábuink egyikével az ellenfélhez közelebbi kezdősorra.
**Egy játékos veszít ha:** elveszti minden bábuját, vagy az ellenfele eléri a kezdősorát egy bábujával.
<h4 id="hu_functions">A program funkciói</h4>
- Táblaméret választás(6x6, 8x8, 10x10)<br>
- Új játék kezdés<br>
- Amíg nem léptél, szabadon válogathatsz a mezőid között és megnézheted 	hová lehet róluk lépni<br>
<i id="host"><b>- Online játék hostolása</b></i><br>
&emsp;1. Új játék -> Játék hostolása<br>
&emsp;2. A szerver IP címét küldd el annak, akivel játszani szeretnél<br>
&emsp;3. Ha partnered készen áll a csatlakozásra, kattints a játékos keresése gombra<br>
&emsp;4. Amint csatlakozott a másik játékos, a játék kezdetét veszi<br>
<i id="join"><b>- Csatlakozás online játékhoz</b></i><br>
&emsp;1. Új játék -> Csatlakozás játékhoz<br>
&emsp;2. Írd be a mezőbe a host IP címét<br>
&emsp;3. Ha partnered készen áll a fogadásodra, kattints a csatlakozás gombra<br>
&emsp;4. Amint csatlakoztál, a játék kezdetét veszi<br>

<h3>Fejlesztés</h3>
<h4 id="known">Ismert problémák</h4>

 - az ip cím ismeretében bárki csatlakozhat a szerverre
 - a hostolási felugró ablak bezárása után nem lehet újra megnyitni(socket already in use)

<h4 id="fixed">Nemrég javított problémák</h4>

 - a host a játék befejezése után nem tudott újra hostolni
 - eltérő kiválasztott táblamérettel nem működött az online játék

<h4 id="planned">Továbbfejlesztési lehetőségek</h4>

 - több táblajáték hozzáadása
 - a hostolási/csatlakozási felugró ablak beépítése



<!--
<h1 align="center">Breakthrough</h1>
<h3 align="center">a board game with online multiplayer capabilities</h3>
<table width="100%" align="center">  
	<tr>  
		<th  width="50%">EN</th>  
		<th>HU</th>   
	</tr>  
	<tr align="center">  
		<td><a href=#en_user>User manual</a></td>
		<td><a href=#hu_user>Felhasználói útmutató</a></td>  
	</tr>
</table>
<table align="center" width="100%">
	<tr>  
		<th>Development:</th>  
	</tr> 
	</tr>
	<tr align="center">  
		<td><a href=#known>Known issues</a></td>  
	</tr> 
	<tr align="center">  
		<td><a href=#fixed>Recently fixed issues</a></td>  
	</tr>  
	<tr align="center">  
		<td><a href=#planned>Planned improvements</a></td>  
	</tr>  
</table>

<h3 id="en_user">User manual</h3>
<div><a href="#en_about">About the game</a></div>
<div><a href="#en_multi">Multiplayer guide</a></div>
<h4 id="en_about>About the game</h4>
Breakthrough is a two player board game. Each player starts the game with two rows of pieces. Red is the starting player. Turns alternate, with each player moving one piece per turn.
**A player's piece may move one square:**

 - **straight forward&ast;** or **diagonally forward** if the targeted square is **empty** *(forward in the direction of the opponent's home row)*
 - **diagonally forward** if the targeted square is **occupied by the opponent's piece**
&ast;*forward means the direction towards the opponent's home row*

**Goal:** reach the opponent's home row with one of your pieces.
**A player loses** if they lose all of their pieces or the other player reaches their home row.
-->
