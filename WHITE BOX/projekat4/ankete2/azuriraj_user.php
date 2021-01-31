<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once("header.php");
if(isset($_POST['azurdugme'])){
    $u=$_POST['user'];
    $u_staro=$_POST['u'];
    $p=$_POST['pass'];
    $e=$_POST['email'];
    $t=$_POST['tip'];
    $q="UPDATE korisnik SET username='".$u."',password='".$p."',email='".$e."',tip='".$t."' WHERE username='".$u_staro."';";
    $qr=qq($q);
    header("location:admin.php");
}

if(isset($_GET["user"])) {
    $user = $_GET["user"];
    $q = "SELECT * FROM korisnik WHERE username='".$user."';";
    $qr = qq($q);
    if($qr->num_rows>0){
        $n=$qr->fetch_assoc();
        ?>
<form class="ui form"  id="azurform" name="azurform" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="">
<div class="field">
    <label>Username</label>
    <input type="text" name="user" value="<?php echo $n['username'];?>" required>
</div>
    <div class="field">
        <label>Password</label>
        <input type="password" name="pass" value="<?php echo $n['password'];?>" required>
    </div>
    <div class="field">
        <label>Email</label>
        <input type="email" name="email" value="<?php echo $n['email'];?>" required>
    </div>
    <div class="field">
        <label>Tip korisnika(1-ispitanik,2-sluzbenik,3-autor)</label>
        <input type="number" name="tip" value="<?php echo $n['tip'];?>" min="1" max="3" required>
    </div>
    <input type="hidden" name="u" value="<?php echo $user;?>">
    <button class="ui primary button" name="azurdugme" type="submit">AZURIRAJ</button>
</form>
<?php
    }
}