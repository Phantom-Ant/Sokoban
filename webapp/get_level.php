<?php
header('Content-Type: application/json');

$host = 'localhost';
$user = 'root';
$password = 'root';
$dbname = 'sokoban';

$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    echo json_encode(['error' => 'Connessione fallita: ' . $conn->connect_error]);
    exit();
}

$level_id = isset($_GET['level']) ? intval($_GET['level']) : 1;

$query = "SELECT * FROM Livello WHERE id_livello = ?";
$stmt = $conn->prepare($query);
$stmt->bind_param("i", $level_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result && $result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $map = json_decode($row['info_livello'], true);
    $player = findPlayer($map);
    echo json_encode(['map' => $map, 'player' => $player]);
} else {
    echo json_encode(['error' => 'Livello non trovato']);
}

$stmt->close();
$conn->close();

function findPlayer($map) {
    for ($y = 0; $y < count($map); $y++) {
        for ($x = 0; $x < count($map[$y]); $x++) {
            if ($map[$y][$x] == 2) {
                return ['x' => $x, 'y' => $y];
            }
        }
    }
    return ['x' => 0, 'y' => 0];
}
?>
