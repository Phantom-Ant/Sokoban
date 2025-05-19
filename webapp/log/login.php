<?php
require '../php/log.php';

$mail = $_POST['mail'];
$password = $_POST['password'];

$rest = $con->query("select count(*) from users where users.mail = '$mail' and users.password = '$password';");

$row = $rest->fetch_row();


echo $row[0];

if( $row > 0){
    $con->close();
    header('Location:/levels/levels.html');
    exit;
}else{
    echo '<h1>3<0</h1>';
}
$con->close();
?>