import './App.css';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import UserList from "./components/users-component/UserList";
import Header from "./components/header/Header";
import AddUser from "./components/users-component/AddUser";
import EditUser from "./components/users-component/EditUser";
import IncomingDocumentList from "./components/incoming-document-component/IncomingDocumentList";
import AddIncomingDocument from "./components/incoming-document-component/AddIncomingDocument";
import EditIncomingDocument from "./components/incoming-document-component/EditIncomingDocument";
import RotationList from "./components/rotation-component/RotationList";
import AddRotation from "./components/rotation-component/AddRotation";
import EditRotation from "./components/rotation-component/EditRotation";
import RotationListOfDocument from "./components/rotation-component/RotationListOfDocument";
import DocumentAmountReceiverList from "./components/incoming-document-component/DocumentAmountReceiverList";
import firebase from "./firebase";
import {useEffect, useState} from "react";
import 'antd/dist/antd.css';

function App() {
    const [tokenVal, setTokenVal] = useState('');

    useEffect(() => {
        const messaging = firebase.messaging();
        messaging.requestPermission().then(() => {
            return messaging.getToken();
        }).then(token => {
            setTokenVal(token);
            console.log("Token: " + token);
        }).catch(() => {
            console.log("error")
        })
    })

    return (
        <>
            <Header/>
            <Router>
                <Routes>
                    <Route path="/" exact component={App} />
                    <Route path="/user-list" component={UserList} element={<UserList/>}/>
                    <Route path="/user-add" component={AddUser}  element={<AddUser token={tokenVal}/>}/>
                    <Route path="/user-edit/:id" component={EditUser}  element={<EditUser/>}/>

                    <Route path="/document-list" component={IncomingDocumentList}  element={<IncomingDocumentList/>}/>
                    <Route path="/document-add" component={AddIncomingDocument}  element={<AddIncomingDocument/>}/>
                    <Route path="/document-edit/:id" component={EditIncomingDocument}  element={<EditIncomingDocument/>}/>

                    <Route path="/rotation-list" component={RotationList} element={<RotationList/>}/>
                    <Route path="/rotation-add" component={AddRotation}  element={<AddRotation/>}/>
                    <Route path="/rotation-edit/:id" component={EditRotation}  element={<EditRotation/>}/>
                    <Route path="/rotation-list-of-document/:id" component={RotationListOfDocument}  element={<RotationListOfDocument/>}/>
                    <Route path="/document-amount-receiver-list" component={DocumentAmountReceiverList}  element={<DocumentAmountReceiverList/>}/>
                </Routes>
            </Router>
        </>
    );
}

export default App;
