const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
    name: String,
    email: String,
    password: String
});

const Model = mongoose.model("userSchema", userSchema);
module.exports = Model;