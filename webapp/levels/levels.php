<?php
$host = 'localhost';
$user = 'root';
$password = 'root';
$dbname = 'sokoban';

$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["error" => "Connessione fallita"]);
    exit;
}

$query = "SELECT id_livello, nome, dimX, dimY, publisher FROM Livello ORDER BY id_livello ASC";
$result = $conn->query($query);

$livelli = [];
while ($row = $result->fetch_assoc()) {
    $livelli[] = $row;
}

header('Content-Type: application/json');
echo json_encode($livelli);

$conn->close();
?>
