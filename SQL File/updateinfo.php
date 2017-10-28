<?php
	$user_name = "root";
	$user_pass = "";
	$host_name = "localhost";
	$db_name = "dpupload";

	$con = mysqli_connect($host_name, $user_name, $user_pass, $db_name);

	if($con) {

		$file = $_POST['file'];
		$name = $_POST['name'];

		$sql = "INSERT INTO fileinfo(name) values ('$name')";
		$upload_path = "uploads/$name";

		if(mysqli_query($con, $sql)) {

			file_put_contents($upload_path, base64_decode($file));
			echo json_encode(array('response'=>'File Uploaded Successfully'));

		} else {

			echo json_encode(array('response'=>'File Uploade Failed'));

		}

	} else {

		echo json_encode(array('response'=>'File Upload Failed'));

	}

	mysqli_close($con);

?>
