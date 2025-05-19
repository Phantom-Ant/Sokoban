<?php
<<<<<<< HEAD
require '../php/log.php';

$mail = $_POST['mail'];
$password = $_POST['password'];

$rest = $con->query("select count(*) from users where users.mail = '$mail' and users.password = '$password';");

$row = $rest->fetch_row();


=======
require '../log/log.php';

$email = $_POST['email'];
$password = $_POST['password'];

$password = md5($password);

$rest = $con->query("select count(*) from users where users.email = '$email' and users.password = '$password';");

$row = $rest->fetch_row();
>>>>>>> 328c8a3 (add titles)
echo $row[0];

if( $row > 0){
    $con->close();
<<<<<<< HEAD
    header('Location:/levels/levels.html');
=======
    header('Location:../levels/levels.html');
>>>>>>> 328c8a3 (add titles)
    exit;
}else{
    echo '<h1>3<0</h1>';
}
$con->close();
?>