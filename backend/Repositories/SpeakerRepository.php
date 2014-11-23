<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utilities;

class SpeakerRepository {
    /**
     * @return array
     */
    public static function  getSpeakers()
    {
        $sql_query = "SELECT * FROM speaker";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function getSpeakerById($id)
    {
        $sql_query = "SELECT * FROM speaker WHERE id=:id;";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    public static function insertSpeaker($id, $name)
    {
        $sql_query = "INSERT INTO speaker VALUES (:id,:name);";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }

    public static function updateSpeaker($id, $name)
    {
        $sql_query = "UPDATE speaker SET name=:name WHERE id=:id;";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':name'=>$name,':id'=>$id));
    }
} 