<?php
<<<<<<< HEAD
require '../php/log.php';
$name = $_POST['name'];
$surname = $_POST['surname'];
$mail = $_POST['mail'];
$password = $_POST['password'];

$rest = $con -> query("insert into users(name,surname,mail,password) values ('$name','$surname','$mail','$password')");
if($rest){
    $con->close();
    header('Location: ../html/login.html');
=======
require '../log/log.php';
$username = $_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];

$password = md5($password);
$rest = $con -> query("insert into users(username,email,password) values ('$username','$email','$password')");
if($rest){
    $con->close();
    header('Location: ../log/login.html');
>>>>>>> 328c8a3 (add titles)
    exit;
}else{
    echo("no");
}
<<<<<<< HEAD

=======
>>>>>>> 328c8a3 (add titles)
$con->close();
?>