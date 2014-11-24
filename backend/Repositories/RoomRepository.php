<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:06
 */

namespace Repositories;

use Utilities\Utilities;

/**
 * The repository contains all methods for interacting with the database for the Room model
 *
 * Class RoomRepository
 * @package Repositories
 */
class RoomRepository{

    /**
     * @return array Returns all rooms
     */
    public static function getRooms(){
        $sql_query = "SELECT * FROM room;";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $id int The id of the room
     * @return mixed|null Returns the room if found else null is returned
     */
    public static function getRoomById($id)
    {
        $sql_query = "SELECT * FROM room WHERE id=:id;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    /**
     * @param $id int The id of the room
     * @param $name string The name of the room
     * @return bool Returns true if successful
     */
    public static function insertRoom($id, $name)
    {
        $sql_query = "INSERT INTO room VALUES (:id,:name);";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }

    /**
     * @param $id int The id of the room
     * @param $name string The name of the room
     * @return bool Returns true if successful
     */
    public static function updateRoom($id, $name)
    {
        $sql_query = "UPDATE room SET name=:name WHERE id=:id;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':name'=>$name,':id'=>$id));
    }



}