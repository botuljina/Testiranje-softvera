<?php
if (!isset($_SESSION))
    session_start();
if (!isset($_SESSION['tip']))
    header("location: login.php");
require_once 'inc/database.php';
require_once 'header.php';
require_once 'sort.php';
require_once 'inc/funkcije.php';
require_once 'inc/update_table.php';
if(isset($_GET['list'])){echo "<script>sluzbenik_listic=1</script>";}else {echo "<script>sluzbenik_listic=0</script>";}
$listic=0;
?>
<script>
    proc=0;
    pop=0;
    rez=null;
</script>

<?php
if (isset($_GET['pop'])){echo "<script>pop=1;</script>";}
if(isset($_GET['id'])&&isset($_GET['tip'])&&isset($_GET['ida'])&&isset($_GET['anon'])){
    prikaz_za_autora($_GET['tip'],$_GET['id'],$_GET['ida'],$_GET['anon']);
}
    if(isset($_GET['s'])){


        if (!isset($_GET['ida'])) {
            header_tabele();
        } else {
            if(isset($_GET['listic'])){$listic=1;}
            prikazi_anketu_sluzb($_GET['ida'],$listic);
        }
    }else {
        if (!isset($_GET['ida'])) {
            header_tabele();
        } else {
            provera_popunjena($_GET['ida']);
            prikazi_anketu($_GET['ida']);
        }
    }


function prikaz_za_autora($tip,$id,$ida,$anon){
        if($tip=='1') {
            $q = "SELECT * FROM ispitanik_anketa WHERE idPopunjavanje='" . $id . "' AND idAnketa='" . $ida . "';";

        }else{
            $q = "SELECT * FROM listic_anketa WHERE idListic='" . $id . "' AND idAnketa='" . $ida . "';";
        }
    $qr = qq($q);

if($n=$qr->fetch_assoc()){
    if($tip==1){
        if($anon==0)
        echo "<h3>".$n['username']."</h3>";
        else
        echo "<h3>Anonimno</h3>";
    }else
        echo "<h3>".$n['ime']."</h3>";
    ?>

    <script>
        rez=<?php echo $n['rezultat']; ?>;
        console.log("novi",rez);
        pop=1;
    </script> <?php }
}
function provera_popunjena($ida){
        $q="SELECT * FROM ispitanik_anketa WHERE username='".$_SESSION['kime']."' AND idAnketa='".$ida."';";
        $qr=qq($q);
    ?>

    <?php
        if($n=$qr->fetch_assoc()){?>
            <script>
                rez=<?php echo $n['rezultat']; ?>;
            </script>


            <?php
            if($n['popunjena']==1){
                ?>
                <script>
                    pop=1;

                    console.log('IMA POPUNJENA');
                </script>
<?php
            }
        }
}
        ?>

        <script>

            function osveziTabelu(data) {

                $.get('popunjena.php', {}, function (data1) {
                    //alert('ajax completed. Response:  '+data);
                    data1 = JSON.parse(data1);
                    // console.log(data1);
                    pop = data1;
                    prosledi(pop);
                    //do after submission operation in DOM
                });

                function prosledi(pop) {
                    document.getElementById('tbody').innerHTML = '';
                    var i = 0;
                    table = '';

                   // console.log(pop);
                    while (niz = data[i]) {
                        var row = "";
                        row = napraviRed(niz, pop);
                        table += row;
                        i++;
                    }
                    table += "</tbody>\n";
                    table += "</table>\n";
                    document.getElementById('tbody').innerHTML = table;
                    dodaj_linkove();
                }
            }

            function napraviRed(niz, pop) {
                id_pop = 0;
                popunjena = 0;
                while (pop[id_pop]) {
                    if (niz['idAnketa'] === pop[id_pop]['idAnketa']) {
                        popunjena = parseInt(pop[id_pop]['popunjena']);

                    }
                    id_pop++;
                }
                //console.log('popunjena',popunjena);
                var row = "";

                row += "<tr>\n";
                row += "<td><a class='linkdugme' ida='" + niz['idAnketa'] + "'>" + niz['naziv'] + "</a></td>";
                //row+="<td><button class='linkdugme'>"+niz['naziv']+"</button></td>";
                row += "<td>" + niz['datum_pocetka'] + "</td>";
                row += "<td>" + niz['datum_kraja'] + "</td>";
                if (popunjena !== 0) {
                    row += "<td><i class='icon checkmark'></i>Popunjena</td>";
                } else {
                    row += "<td class='positive'><i class='icon close'></i> Nije popunjena</td>"
                }
                row += "<td>" + niz['autor'] + "</td>";
                row += "</tr>\n";

                return row;

            }

            $(document).ready(function () {
                $('#sort1').change(function () {
                    //Selected value
                    var inputValue = $(this).val();
                    var inputValue1 = document.getElementById('sort2').value;
                    //alert("value in js "+inputValue);
                    //console.log(inputValue);
                    //console.log(inputValue1);
                    //Ajax for calling php function
                    console.log('/sort.php/inputValue='+inputValue+'&inputValue1='+inputValue1);
                    $.get('sort.php', {inputValue: inputValue, inputValue1: inputValue1}, function (data) {
                        data = JSON.parse(data);
                        console.log(data);
                        osveziTabelu(data);
                        // alert('ajax completed. Response:  '+data);
                        //do after submission operation in DOM
                    });
                });
            });
            $(document).ready(function () {
                $('#sort2').change(function () {
                    //Selected value
                    var inputValue = document.getElementById('sort1').value;
                    var inputValue1 = $(this).val();
                    //alert("value in js "+inputValue1);
                    //console.log(inputValue);
                    //console.log(inputValue1);
                    //Ajax for calling php function
                    $.get('sort.php', {inputValue: inputValue, inputValue1: inputValue1}, function (data) {
                        //alert('ajax completed. Response:  '+data);
                        data = JSON.parse(data);
                        console.log(data);
                        osveziTabelu(data);
                        //do after submission operation in DOM
                    });
                });
            });

            $(document).ready(function () {
                dodaj_linkove();
            });

            function dodaj_linkove() {
                var cnt = 0;
                $('.linkdugme').click(function () {
                    var ida = $(this).attr('ida');
                    console.log(ida);
                    listic="";
                    if(sluzbenik_listic==1)listic="&s&listic";
                    // $.get('pretragaAnketa.php', { ida: ida}, function(data){
                    //     console.log(data);
                    // });
                    window.location.href = 'pretragaAnketa.php?ida=' + ida+listic;
                });

            }

        </script>
        <?php
//    }
require_once 'footer.php';
?>

