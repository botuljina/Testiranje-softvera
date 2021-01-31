<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');
if(isset($_POST['reddugme1'])){
    $red=$_POST['red'];
//    $num=$_POST['num'];
    $idp = $_POST["idp"];
    if($red=='a'){
        $q="SELECT * FROM potpitanja  WHERE idPitanja = '".$idp."';";
        $qr=qq($q);
        $broj=$qr->num_rows;
        $niz=range(1,$broj);
        shuffle($niz);
        $r=0;
        while($r<$broj){
            $n=$qr->fetch_assoc();
            $q1="UPDATE potpitanja SET redpotp='".$niz[$r]."' WHERE idPitanja='".$idp."' AND idPotpitanja='".$n['idPotpitanja']."'";
            $qr1=qq($q1);
            $r++;
        }
//        $q="SELECT * FROM anketa_pitanja  WHERE idAnketa = '".$idA."' ORDER BY FIELD(red,'".$niz."');"; //RESITI OVAJ QUERY!!!
//        $qr=qq($q);
//        while($n=$qr->fetch_assoc()){
//            $q1="UPDATE anketa_pitanja SET idPitanja='".$n['idPitanja']."' WHERE idAnketa='".$idA."';";
//            $qr=qq($q1);
//        }
    }elseif ($red=='r') {
        $num = $_POST['num'];
        $idp = $_POST['idp'];
        $idpp=$_POST['potp'];
        $m = 0;
        foreach ($num as $n) {
            $q1 = "UPDATE potpitanja SET redpotp='" . $n . "' WHERE idPitanja='" . $idp . "' AND idPotpitanja='" . $idpp[$m] . "';";
            $qr1 = qq($q1);
            $m++;
        }
    }
    header("location: autor.php");

}
if (isset($_GET["idp"])) {
    $idp = $_GET["idp"];
//    $q = "SELECT * FROM anketa_pitanja WHERE idAnketa='".$ank."' AND idPitanja='".$pit."';";
//    $qr=qq($q);
//    if($qr->num_rows==0){
//        $q="INSERT INTO anketa_pitanja (idAnketa,idPitanja) VALUES('".$ank."','".$pit."');";
//        $qr=qq($q);
//    }
    ?>
    <form name="formred1" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="">
        <div class="field">
            <label for="red">Izaberi nacin premestanja potpitanja: </label>
            <select id="red" name="red" class="ui dropdown">
                <option value="-1"></option>
                <option value="r">Rucno</option>
                <option value="a">Automatski</option>
            </select>
        </div>
        <input type="hidden" name="idp" value="<?php echo $idp;?>">
        <div class="field hidden" id="rucno">
            <?php
            $q = "SELECT * FROM potpitanja  INNER JOIN pitanja 
                ON potpitanja.idPitanja =pitanja.idPitanja
                WHERE potpitanja.idPitanja='" . $idp . "' ORDER BY redpotp;";
            $qr=qq($q);
            $br=$qr->num_rows;
            if($br>0){$i=0;
                while($n=$qr->fetch_assoc()){
                    ?>
                    <div class="field">
                        <label class=""> <?php echo $n['potpitanje'];?></label>
                        <input type="number" name="num[<?php echo $i;?>]" class="ui input" min="1" max="<?php echo $br;?>">
                        <input type="hidden" name="potp[<?php echo $i;?>]" value="<?php echo $n['idPotpitanja'];?>">
                    </div>
                    <?php
                    $i++;
                }
            }
            ?>

        </div>
        <button class="ui button primary" name="reddugme1" type="submit">IZMESAJ</button>
    </form>
    <?php
}
?>
<script>
    $(document).ready(function(){
        $('#red').change(function(){
            //Selected value
            var inputValue = $(this).val();
            if(inputValue==='r'){
                document.getElementById('rucno').style.display='block';
            }
            if(inputValue==='a'){
                document.getElementById('rucno').style.display='none';
            }
            // var inputValue1 = document.getElementById('sort2').value;
            //alert("value in js "+inputValue);
            //console.log(inputValue);
            //console.log(inputValue1);
            //Ajax for calling php function
            // $.get('sort.php', { inputValue: inputValue }, function(data){
            //     // data=JSON.parse(data);
            //     // console.log(data);
            //
            //     // alert('ajax completed. Response:  '+data);
            //     //do after submission operation in DOM
            // });
        });
    });
</script>