<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utilities;

class TalkSpeakerRepository {
    /**
     * @return array
     */
    public static function  getTalkSpeakers()
    {
        $sql_query = "SELECT * FROM talk_speaker";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function insertTalkSpeaker($talkId, $speakerId)
    {
        try {
            $sql_query = "INSERT INTO talk_speaker (talk_id, speaker_id) VALUES (:talk_id,:speaker_id);";
            $con = Utilities::getConnection();
            $stmt = $con->prepare($sql_query);
            return $stmt->execute(array(':talk_id' => $talkId, ':speaker_id' => $speakerId));
        }catch (\Exception $ex){
            return false;
        }
    }
} 