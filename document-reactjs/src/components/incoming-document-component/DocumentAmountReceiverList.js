import React, {useEffect, useState} from 'react';
import Col from "react-bootstrap/Col";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import {MDBTable, MDBTableBody, MDBTableHead} from "mdb-react-ui-kit";
import Row from "react-bootstrap/Row";
import axios from "axios";
import Dropdown from "react-bootstrap/Dropdown";
import DropdownButton from "react-bootstrap/DropdownButton";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const DocumentAmountReceiverList = () => {

    const [ documentAmountReceived, setDocumentAmountReceived] = useState([]);
    const [success, setSuccess] = useState('');

    useEffect(() => {
        axios.get("http://localhost:8080/api/rotation/document-amount-received-list")
            .then((data) => {
                setDocumentAmountReceived(data.data.data)
            })
    })

    const exportToHTML = () => {
        axios.post("http://localhost:8080/api/rotation/document_amount_receiver_list-report", {"format": "html"})
            .then((data) => {
                setSuccess(data.data.message);
                addFlag.call();
            })
    }

    const exportToPDF = () => {
        axios.post("http://localhost:8080/api/rotation/document_amount_receiver_list-report", {"format": "pdf"})
            .then((data) => {
                setSuccess(data.data.message);
                addFlag.call();
            })
    }

    const [flags, setFlags] = React.useState([]);

    const addFlag = () => {
        const newFlagId = flags.length + 1;
        const newFlags = flags.slice();
        newFlags.splice(0, 0, newFlagId);
        setFlags(newFlags);
    };

    const handleDismiss = () => {
        setFlags(flags.slice(1));
    };

    const documentAmountReceivedList = documentAmountReceived.map(document => {
        return <tr key={document.receiverName}>
            <td>{document.receiverName}</td>
            <td>{document.documentAmount}</td>
        </tr>
    })

    return (
        <>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <DropdownButton title="Tải xuống"  size="sm" id="bg-nested-dropdown">
                        <Dropdown.Item onClick={exportToHTML}  eventKey="1">HTML</Dropdown.Item>
                        <Dropdown.Item onClick={exportToPDF} eventKey="2">PDF</Dropdown.Item>
                    </DropdownButton>
                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>Người nhận</th>
                                <th scope='col'>Số lượng văn bản nhận được</th>
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {documentAmountReceivedList}
                        </MDBTableBody>
                    </MDBTable>
                </Col>
            </Row>

            <FlagGroup onDismissed={handleDismiss}>
                {flags.map((flagId) => {
                    return (
                        <AutoDismissFlag
                            appearance="success"
                            id={flagId}
                            icon={
                                <SuccessIcon
                                    label="Success"
                                    secondaryColor={token('color.background.success.bold', G400)}
                                />
                            }
                            key={flagId}
                            title={success}
                        />
                    );
                })}
            </FlagGroup>
        </>
    );
};

export default DocumentAmountReceiverList;