var gclient = require("googleapis"),
	//YOU must create this file to return the appropriate object or update the gconfig values below to hardcode (not recommended for security).
	gconfig = {
		CLIENT_ID: '303214978933-se35u7a4p39bqt742ma4dg0m0l7ucur3.apps.googleusercontent.com',
		SERVICE_EMAIL: 'eugenejahnjahn@gmail.com', //
		JSON_FILE_PATH: './yourServiceAccountKeyFile.json' //Key file provided from setup above
	},
	jwtClient = new gclient.auth.JWT(
		gconfig.CLIENT_ID,
		gconfig.JSON_FILE_PATH,
		null, ['https://www.googleapis.com/auth/drive',
			// 'https://www.googleapis.com/auth/drive.appdata',
			// 'https://www.googleapis.com/auth/drive.apps.readonly',
			// 'https://www.googleapis.com/auth/drive.file',
			// 'https://www.googleapis.com/auth/drive.metadata',
			// 'https://www.googleapis.com/auth/drive.metadata.readonly',
			// 'https://www.googleapis.com/auth/drive.readonly',
			// 'https://www.googleapis.com/auth/drive.scripts',
			'https://www.googleapis.com/auth/admin.reports.audit.readonly'
		],
		gconfig.SERVICE_EMAIL
	); // I think for an insert, this doesn't actually control the owner but it's required and I'm fuzzy on the purpose


function insertTest(drive) { //Need to figure out how to specify owner because the service accont owns this file right now which isn't very useful
	drive.files.insert({
			resource: {
				title: 'I was created by Node.js!',
				mimeType: 'text/plain'
			},
			media: {
				mimeType: 'text/plain',
				body: 'Ipsum Lorem!'
			}
		},
		function(err, resp) {
			if (err) {
				console.log('insert error: ', err);
			} else {
				console.log('File created. See id following:');
				console.dir(resp);
			}
		}
	);
}

jwtClient.authorize(function(err, tokens) {
	if (err) {
		console.log("Error authorizing with JWT", err);
		return;
	}

	var drive = gclient.drive({
		version: 'v2',
		auth: jwtClient
	});

	insertTest(drive);
});
