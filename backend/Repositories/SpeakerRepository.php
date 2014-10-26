<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utitilies;

class SpeakerRepository {
    /**
     * @return array
     */
    public static function  getSpeakers()
    {
        $sql_query = "SELECT * FROM speaker";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function insertSpeaker($id, $name)
    {
        $sql_query = "INSERT INTO speaker VALUES (:id,:name);";
        $con = Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }
} 