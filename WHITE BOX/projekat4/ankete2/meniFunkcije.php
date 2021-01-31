<?php
if (!isset($_SESSION))
    session_start();
if (!isset($_SESSION['tip']))
    header('location: login.php');

function meniIsp(){
    ?>
    <li>
        <a href="pretragaAnketa.php">Ankete</a>
    </li>

    <?php
}
function meniSl(){
    ?>
    <li>
        <a href="pretragaAnketa.php">Ankete</a>
    </li>
    <li>
        <a href="pretragaAnketa.php?list=">Popuni listice</a>
    </li>

    <?php
}
function meniAutor(){
    ?>
    <li>
        <a href="pretragaAnketa.php">Ankete</a>
    </li>
    <li>
        <a href="pretragaAnketa.php?list=">Popuni listice</a>
    </li>
    <li>
        <a href="autor.php">Moje ankete</a>
    </li>
    <li>
        <a href="pregledAnketa.php">Pregled anketa</a>
    </li>

    <?php
}
    function meniAdmin(){
    ?>
    <li>
        <a href="pretragaAnketa.php">Ankete</a>
    </li>
        <li>
            <a href="pretragaAnketa.php?list=">Popuni listice</a>
        </li>
        <li>
            <a href="autor.php">Moje ankete</a>
        </li>
        <li>
            <a href="admin.php">Aktivni korisnici</a>
        </li>
        <li>
            <a href="adminReg.php">Registracije</a>
        </li>
        <?php
}

