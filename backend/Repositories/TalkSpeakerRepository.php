<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utilities;

/**
 * The repository contains all methods for interacting with the database for the TalkSpeaker model
 *
 * Class TalkSpeakerRepository
 * @package Repositories
 */
class TalkSpeakerRepository {
    /**
     * @return array Return all talk_speakers
     */
    public static function  getTalkSpeakers()
    {
        $sql_query = "SELECT * FROM talk_speaker";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $talkId int The talkid of the talk_speaker
     * @param $speakerId int the speakerid of the talk_speaker
     * @return bool Returns true if successful
     */
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