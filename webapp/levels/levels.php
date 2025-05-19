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

<<<<<<< HEAD
$query = "SELECT id_livello, nome, dimX, dimY, publisher FROM Livello ORDER BY id_livello ASC";
$result = $conn->query($query);

$livelli = [];
while ($row = $result->fetch_assoc()) {
    $livelli[] = $row;
=======
$query = "SELECT id, name, data, publisher FROM levels ORDER BY id ASC";
$result = $conn->query($query);

if (!$result) {
    http_response_code(500);
    echo json_encode(["error" => "Errore nella query: " . $conn->error]);
    exit;
}

$livelli = [];
while ($row = $result->fetch_assoc()) {
    $lines = explode("\n", trim($row['data']));
    $dimY = count($lines);
    $dimX = max(array_map('strlen', $lines));

    $livelli[] = [
        'id_livello' => $row['id'],
        'nome' => $row['name'],
        'publisher' => $row['publisher'],
        'dimX' => $dimX,
        'dimY' => $dimY,
        'data' => str_replace("\n", "\\n", $row['data'])
    ];
>>>>>>> 328c8a3 (add titles)
}

header('Content-Type: application/json');
echo json_encode($livelli);
<<<<<<< HEAD

=======
>>>>>>> 328c8a3 (add titles)
$conn->close();
?>
