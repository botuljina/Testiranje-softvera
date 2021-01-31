<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');

if(isset($_POST['reddugme'])){
   print_r($_POST);
    $red=$_POST['red'];

    $idA = $_POST["ida"];
    if($red=='a'){
        $q="SELECT * FROM anketa_pitanja  WHERE idAnketa = '".$idA."';";
        $qr=qq($q);
        $broj=$qr->num_rows;
        $niz=range(1,$broj);
//         print_r($niz);
        shuffle($niz);
        $r=0;
        while($r<$broj){
            $n=$qr->fetch_assoc();
            $q1="UPDATE anketa_pitanja SET red='".$niz[$r]."' WHERE idAnketa='".$idA."' AND idPitanja='".$n['idPitanja']."';";
            $qr1=qq($q1);
            $r++;
        }
    }elseif ($red=='r') {
        $num = $_POST['num'];
        $idp = $_POST['idp'];
        $m = 0;
        foreach ($num as $n) {
            $q1 = "UPDATE anketa_pitanja SET red='" . $n . "' WHERE idAnketa='" . $idA . "' AND idPitanja='" . $idp[$m] . "';";
            $qr1 = qq($q1);
            $m++;
        }
    }
    header("location: autor.php");
}
if (isset($_GET["ida"])) {
    $idA = $_GET["ida"];
//    $q = "SELECT * FROM anketa_pitanja WHERE idAnketa='".$ank."' AND idPitanja='".$pit."';";
//    $qr=qq($q);
//    if($qr->num_rows==0){
//        $q="INSERT INTO anketa_pitanja (idAnketa,idPitanja) VALUES('".$ank."','".$pit."');";
//        $qr=qq($q);
//    }
?>
    <form class="ui form" name="formred" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="">
        <div class="field">
    <label for="red">Izaberi nacin premestanja pitanja: </label>
<select id="red" name="red" class="ui dropdown">
    <option value="-1"></option>
    <option value="r">Rucno</option>
    <option value="a">Automatski</option>
</select>
        </div>
        <input type="hidden" name="ida" value="<?php echo $idA;?>">
        <div class="field hidden" id="rucno">
            <?php
            $q = "SELECT * FROM anketa_pitanja  INNER JOIN pitanja 
                ON anketa_pitanja.idPitanja =pitanja.idPitanja
                WHERE anketa_pitanja.idAnketa='" . $idA . "' ORDER BY red;";
            $qr=qq($q);
            $br=$qr->num_rows;
            if($br>0){$i=0;
                while($n=$qr->fetch_assoc()){
                    ?>
                    <div class="field">
                    <label> <?php echo $n['tekst'];?></label>
                    <input type="number" name="num[<?php echo $i;?>]" class="ui input" min="1" max="<?php echo $br;?>">
                        <input type="hidden" name="idp[<?php echo $i;?>]" value="<?php echo $n['idPitanja'];?>">
                    </div>
                    <?php
                $i++;
                }
            }
            ?>

        </div>
    <button class="ui button primary" name="reddugme" type="submit">IZMESAJ</button>
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
