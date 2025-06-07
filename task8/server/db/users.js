const users = [];

function findOrCreateUser(googleProfile) {
    let user = users.find((u) => u.googleId === googleProfile.googleId);
    if (!user) {
        user = {
            id: users.length + 1,
            googleId: googleProfile.googleId,
            email: googleProfile.email,
            name: googleProfile.name,
        };
        users.push(user);
    }
    return user;
}

module.exports = { findOrCreateUser };