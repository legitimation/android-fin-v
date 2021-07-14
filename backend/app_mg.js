var express     = require('express');
var app         = express();
var bodyParser  = require('body-parser');
var mongoose    = require('mongoose');
var User = require('./src/models/user');
var Post = require('./src/models/post');
var Comment=require('./src/models/comment');

const dbURL = 'mongodb://localhost:27017/myDb'

var db = mongoose.connection;

db.on('error', function(){
    console.log('Connection Failed!');
});
db.once('open', function() {
    console.log('DB Connected!');
});


mongoose.connect(dbURL, {
    useNewUrlParser: true,
    useUnifiedTopology: true
  });


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));

var port = process.env.PORT || 80;


var router_user = require('./src/routes/index_user')(app, User);

var router_post = require('./src/routes/index_post')(app, Post);

var router_comment = require('./src/routes/index_comment.js')(app, Comment);

var server = app.listen(port, function(){
    console.log("Express server has started on port " + port)
});


