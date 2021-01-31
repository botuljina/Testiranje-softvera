<?php
require_once 'inc/database.php';
require_once 'header.php';

if (!isset($_SESSION))
    session_start();

$q="SELECT * FROM korisnik;";
$qr=qq($q);
?>
<table class="ui celled  table " id="kortable">
    <thead>
    <tr>
        <th>Username</th>
        <th>Email</th>
        <th>Tip korisnika</th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <?php
    if($qr->num_rows>0){
        while($n=$qr->fetch_assoc()) {
            if ($n['tip'] != 4) {
                ?>
                <tr>
                    <td><?php echo $n['username']; ?></td>
                    <td><?php echo $n['email']; ?></td>
                    <td><?php if($n['tip']==1){echo "Ispitanik"; }
                    elseif ($n['tip']==2){echo "Sluzbenik"; }
                    elseif ($n['tip']==3){echo "Autor"; }
                    ?></td>
                    <td><button class="ui button" value="<?php echo $n['username'];?>" onclick="azuriraj(this.value)" type="button" >AZURIRAJ</button>
                        &nbsp;<button class="ui button brisiuser"  id="" value="<?php echo $n['username'];?>" type="button" >IZBRISI</button></td>
                </tr>
                <?php
            }
        }
    }

    ?>
<script>
    function azuriraj(user){
        alert(1);
    window.location.href="azuriraj_user.php?user="+user;
    }
    $('.brisiuser').click(function(){
        var user=$(this).val();
        $(this).parent().parent().remove();

        // console.log(ida);
        $.get('izbrisi_korisnik.php', { user: user}, function(data){
            // console.log(data);
        });
    });
</script>