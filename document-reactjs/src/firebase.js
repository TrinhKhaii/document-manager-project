import firebase from "firebase";

const firebaseConfig = {
    apiKey: "AIzaSyBCX2e6nQ8ksUJmffYFYbcnV4hKRPdZAVw",
    authDomain: "document-notification-manager.firebaseapp.com",
    projectId: "document-notification-manager",
    storageBucket: "document-notification-manager.appspot.com",
    messagingSenderId: "693754035588",
    appId: "1:693754035588:web:6ed8b468b0cdc22073b1d1"
};
firebase.initializeApp(firebaseConfig)

export default firebase;
