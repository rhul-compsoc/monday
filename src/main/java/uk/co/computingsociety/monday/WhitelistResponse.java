package uk.co.computingsociety.monday;

// [
//   {
//     "id":1,
//     "gameID":"minecraft",
//     "memberID":187979032904728576,
//     "guildID":500612695570120704,
//     "gameUsername":"Hexillium",
//     "gameUserID":"abc-123",
//     "userBanned":false
//   }
// ]

public class WhitelistResponse {
  public long getId() {
    return id;
  }

  public String getGameID() {
    return gameID;
  }

  public long getMemberID() {
    return memberID;
  }

  public long getGuildID() {
    return guildID;
  }

  public String getGameUsername() {
    return gameUsername;
  }

  public String getGameUserID() {
    return gameUserID;
  }

  public boolean isUserBanned() {
    return userBanned;
  }

  private long id;
  private String gameID;
  private long memberID;
  private long guildID;
  private String gameUsername;
  private String gameUserID;
  private boolean userBanned;

  WhitelistResponse(){}
}
