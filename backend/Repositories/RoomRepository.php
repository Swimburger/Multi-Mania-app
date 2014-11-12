<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:06
 */

namespace Repositories;

use Utilities\Utitilies;

class RoomRepository{

    /**
     * @return array
     */
    public static function getRooms(){
        $sql_query = "SELECT * FROM room;";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function getRoomById($id)
    {
        $sql_query = "SELECT * FROM room WHERE id=:id;";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    public static function insertRoom($id, $name)
    {
        $sql_query = "INSERT INTO room VALUES (:id,:name);";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }

    public static function updateRoom($id, $name)
    {
        $sql_query = "UPDATE room SET name=:name WHERE id=:id;";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':name'=>$name,':id'=>$id));
    }



}