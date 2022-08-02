import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import axios from "axios";
import Col from "react-bootstrap/Col";
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';
import {MDBTable, MDBTableBody, MDBTableHead} from "mdb-react-ui-kit";
import Row from "react-bootstrap/Row";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const RotationListOfDocument = () => {
    const {id} = useParams();
    const [rotationList, setRotationList] = useState([]);
    const [success, setSuccess] = useState('');

    useEffect(() => {
        axios.post("http://localhost:8080/api/rotation/rotation-list-of-document", {"id": id})
            .then((data) => {
                setRotationList(data.data.data)
            })
    }, [id]);

    const exportRotationListToHTML = () => {
        axios.post("http://localhost:8080/api/rotation/rotation-list-of-document-report", {"id": id, "format": "HTML"})
            .then((data) => {
                setSuccess(data.data.message)
                addFlag.call();
            })
    }

    const exportRotationListToPDF = () => {
        axios.post("http://localhost:8080/api/rotation/rotation-list-of-document-report", {"id": id, "format": "PDF"})
            .then((data) => {
                setSuccess(data.data.message)
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

    const rotations = rotationList.map(rotation => {
        return <tr key={rotation.id}>
            <td>{rotation.id}</td>
            <td>{rotation.excerpt}</td>
            <td>{rotation.serialNumber}</td>
            <td>{new Intl.DateTimeFormat('en-GB', {
                month: '2-digit',
                day: '2-digit',
                year: 'numeric',
                minute: '2-digit',
                hour: '2-digit',
                second: '2-digit',
            }).format(new Date(rotation.signingDate))}</td>
            <td>{new Intl.DateTimeFormat('en-GB', {
                month: '2-digit',
                day: '2-digit',
                year: 'numeric',
                minute: '2-digit',
                hour: '2-digit',
                second: '2-digit',
            }).format(new Date(rotation.deliveryDate))}</td>
            <td>{rotation.sender}</td>
            <td>{rotation.receiver}</td>
            <td>{rotation.signer}</td>
        </tr>
    })

    return (
        <>
            <Row className="justify-content-md-center">
                <Col md="auto">

                    <DropdownButton title="Tải xuống"  size="sm" id="bg-nested-dropdown">
                        <Dropdown.Item onClick={exportRotationListToHTML}  eventKey="1">HTML</Dropdown.Item>
                        <Dropdown.Item onClick={exportRotationListToPDF} eventKey="2">PDF</Dropdown.Item>
                    </DropdownButton>

                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>Id</th>
                                <th scope='col'>Trích yếu văn bản đến</th>
                                <th scope='col'>Số hiệu</th>
                                <th scope='col'>Ngày ký</th>
                                <th scope='col'>Ngày gửi</th>
                                <th scope='col'>Người gửi</th>
                                <th scope='col'>Người nhận</th>
                                <th scope='col'>Người ký</th>
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {rotations}
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

export default RotationListOfDocument;