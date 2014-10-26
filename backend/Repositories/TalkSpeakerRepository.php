<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utitilies;

class TalkSpeakerRepository {
    /**
     * @return array
     */
    public static function  getTalkSpeakers()
    {
        $sql_query = "SELECT * FROM talk_speaker";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }
} 