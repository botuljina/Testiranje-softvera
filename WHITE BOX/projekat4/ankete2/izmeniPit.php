<?php

if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');


if(isset($_POST['pitsubmit'])){
    $potp=$_POST['novopit'];
    $idp=$_POST['idpit'];
    $q="UPDATE pitanja SET tekst='".$potp."' WHERE idPitanja='".$idp."';";
    $qr = qq($q);
    header("location:autor.php");

}

if(isset($_GET["idp"])) {
    $idp = $_GET["idp"];?>

    <?php


    ?>
    <form class="ui form"  id="pitform" name="pitform" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="">
        <div class="field">
<!--            <label>ID pitanja</label>-->
            <input type="hidden" name="idpit" placeholder="" value="<?php echo $idp; ?>">
        </div>
        <div class="field">
            <label>Novo pitanje</label>
            <input type="text" name="novopit" placeholder="" required value="">
        </div>
        <button class="ui primary button" id="pitsubmit" name="pitsubmit" type="submit">PROMENI</button>
    </form>
    <?php
//    $q = "SELECT * FROM anketa_pitanja  INNER JOIN pitanja
//                ON anketa_pitanja.idPitanja =pitanja.idPitanja
//                WHERE anketa_pitanja.idAnketa='" . $ida . "';";
//    $qr = qq($q);
//    $qr = $qr->fetch_all(MYSQLI_ASSOC);
//    echo json_encode($qr);

}?>