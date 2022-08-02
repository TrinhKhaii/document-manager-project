import React, {useEffect, useState} from 'react';
import {Link, useParams} from "react-router-dom";
import axios from "axios";
import Col from "react-bootstrap/Col";
import {Form, FormGroup, FormText, Input, Label} from "reactstrap";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const EditUser = (props) => {
    const { id } = useParams();
    const [editUser, setEditUser] = useState({
        id: 0,
        userName: ''
    });
    const [error, setError] = useState({userName: ''});
    const [success, setSuccess] = useState([]);

    useEffect(() => {
        axios.post('http://localhost:8080/api/users/find-by-id', ({'id': id}))
            .then((data) => {
                if (data.data.status) {
                    const user = data.data.data[0];
                    setEditUser({id: user.id, userName: user.userName})
                }
            })

    }, [id]);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setEditUser({...editUser, [name]: value});
        setError('');
    }

    const submit = async (event) => {
        event.preventDefault();
        console.log(editUser)
        await axios.post("http://localhost:8080/api/users/update", editUser)
            .then((data) => {
                if (data.status) {
                    setSuccess(data.data.message);
                    addFlag.call()
                }
            }).catch((error) => {
                setError(error.response.data.error);
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

    return (
        <>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <h3>Chỉnh sửa thông tin người dùng</h3>
                    <Form onSubmit={submit}>

                        <FormGroup>
                            <Label for="userName">Name</Label>
                            <Input type="text" name="userName" id="userName" value={editUser.userName || ''}
                                   onChange={handleChange} autoComplete="userName"/>
                            <FormText className={"text-danger"}>
                                {error.userName}
                            </FormText>
                        </FormGroup>
                        <Link to={"/user-list"}>
                            <Button size="sm" variant="danger">Trở về</Button>
                        </Link>
                        <Button size="sm" variant="primary" type="submit">
                            Cập nhật
                        </Button>
                    </Form>
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
}

export default EditUser;