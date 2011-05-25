insert into UserConnection (userId, providerId, profileUrl, accessToken, secret) select member, provider, profileUrl, accessToken, secret from AccountConnection;

