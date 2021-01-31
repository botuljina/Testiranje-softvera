<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');


if(isset($_POST['potpsubmit'])){
    $potp=$_POST['novopotp'];
    $idpotp=$_POST['idpotp'];
    $q="UPDATE potpitanja SET potpitanje='".$potp."' WHERE idPotpitanja='".$idpotp."';";
    $qr = qq($q);

}

if(isset($_GET["idpotp"])) {
    $idpotp = $_GET["idpotp"];?>
    <script>alert(<?php echo $idpotp; ?>);</script>
    <?php


    ?>
<form class="ui form"  id="potpform" name="potpform" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="">
    <div class="field">
        <label>ID potpitanja</label>
        <input type="number" name="idpotp" placeholder="" value="<?php echo $idpotp; ?>">
    </div>
    <div class="field">
        <label>Novo potpitanje</label>
        <input type="text" name="novopotp" placeholder="" required value="">
    </div>
    <button class="ui primary button" id="potpsubmit" name="potpsubmit" type="submit">PROMENI</button>
</form>
<?php
//    $q = "SELECT * FROM anketa_pitanja  INNER JOIN pitanja
//                ON anketa_pitanja.idPitanja =pitanja.idPitanja
//                WHERE anketa_pitanja.idAnketa='" . $ida . "';";
//    $qr = qq($q);
//    $qr = $qr->fetch_all(MYSQLI_ASSOC);
//    echo json_encode($qr);

}?>


