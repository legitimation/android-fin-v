const mongoose = require("mongoose");

const commentSchema = new mongoose.Schema({
    title: String,
    name: String,
    comment: String
});

const Model = mongoose.model("commentSchema", commentSchema);
module.exports = Model;


