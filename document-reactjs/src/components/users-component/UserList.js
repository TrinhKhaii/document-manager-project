import React, {useCallback, useEffect, useState} from 'react';
import { MDBTable, MDBTableHead, MDBTableBody} from 'mdb-react-ui-kit';
import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {Link} from "react-router-dom";
import Pagination from 'react-bootstrap/Pagination';
import { Input, Label} from 'reactstrap';
import axios from "axios";
import Modal from 'react-bootstrap/Modal';
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

import { Table } from 'antd';
const UserList = () => {
    const searchParamForm = {
        name: '',
        pageNumber: 0,
        elementNumber: 4
    }
    const [userList, setUserList] = useState([]);
    const [page, setPage] = useState();
    const [totalPage, setTotalPage] = useState();
    const [searchParam, setSearchParam] = useState(searchParamForm);
    const [searchNameParam, setSearchNameParam] = useState('');
    const [show, setShow] = useState(false);
    const [deleteUser, setDeleteUser] = useState({'id': 0, 'name': ''});
    const [deleteSuccess, setDeleteSuccess] = useState([]);

    useEffect(() => {
        axios.post('http://localhost:8080/api/users/list', searchParam)
            .then((data) => {
                setUserList(data.data.data);
                setPage(data.data.page);
                setTotalPage(Math.ceil(data.data.totalElement / data.data.numberOfElementPerPage));
            })
    }, [searchParam, deleteSuccess]);

    const previousPage = () => {
        setSearchParam({...searchParam, pageNumber: page - 1});
    };

    const nextPage = () => {
        setSearchParam({...searchParam, pageNumber: page + 1});
    };

    const searchHandleChange = (event) => {
        const {value} = event.target;
        setSearchNameParam(value);
    };

    const search = () => {
        setSearchParam({...searchParam, name: searchNameParam});
    };

    const callComfirmDeleteModal = (id, name) => {
        setDeleteUser({id: id, name: name});
        handleConfirmDeleteModalShow();
    }

    const handleConfirmDeleteModalClose = () => setShow(false);
    const handleConfirmDeleteModalShow = () => setShow(true);

    const handleDeleteUser = () => {
        axios.post('http://localhost:8080/api/users/delete', ({'id': deleteUser.id}))
            .then((data) => {
                setDeleteSuccess(data.data.message);
                setDeleteUser({id: 0, name: ''});
                setSearchParam(searchParamForm);
                handleConfirmDeleteModalClose();
                addFlag.call();
            }).catch((err) => {
                handleConfirmDeleteModalClose();
        })
    };

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

    const users = userList.map(user => {
        return <tr key={user.id}>
            <td>{user.id}</td>
            <td>{user.userName}</td>
            <td>
                <Link to={"/user-edit/" + user.id}>
                    <Button size="sm" variant="outline-warning">Sửa</Button>
                </Link>
                <Button size="sm" variant="outline-danger" onClick={() => callComfirmDeleteModal(user.id, user.userName)}>Xóa</Button>
            </td>
        </tr>
    });

    // interface DataType {
    //     key: React.Key;
    //     id: number;
    //     name: string;
    // }

    // const columns: ColumnsType<DataType> = [
    //     {
    //         title: 'Id',
    //         dataIndex: 'id',
    //     },
    //     {
    //         title: 'Name',
    //         dataIndex: 'userName',
    //     }
    // ];
    // const [selectionType, setSelectionType] = useState('checkbox');
    //
    // const rowSelection = {
    //     onChange: (selectedRowKeys: React.Key[], selectedRows: DataType[]) => {
    //         console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    //     },
    //     getCheckboxProps: (record: DataType) => ({
    //         disabled: record.name === 'Disabled User', // Column configuration not to be checked
    //         name: record.name,
    //     }),
    // };

    return (
        <>
            <Row>
                <Col lg={{ span: 4, offset:1  }}>
                    <h3>Danh sách người dùng</h3>
                </Col>
                <Col lg={{ span: 2, offset: 1 }}>
                    <Label for="userNameSearch">Name</Label>
                    <Input type="text" name="name" id="userNameSearch"
                           onChange={searchHandleChange}/>
                </Col>
                <Col lg={{ span: 2, offset: 1 }}>
                    <Button size="sm" variant="primary" type="submit" onClick={search}>
                        Tìm kiếm
                    </Button>
                </Col>
            </Row>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <Link to={"/user-add"}>
                        <Button size="sm" variant="outline-success">Thêm mới</Button>
                    </Link>
                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>Id</th>
                                <th scope='col'>Tên</th>
                                <th scope='col'>Hành động</th>
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {users}
                        </MDBTableBody>
                    </MDBTable>
                    {/*<Table*/}
                    {/*    rowSelection={{*/}
                    {/*        type: selectionType,*/}
                    {/*        ...rowSelection,*/}
                    {/*    }}*/}
                    {/*    columns={columns}*/}
                    {/*    dataSource={userList}*/}
                    {/*/>*/}
                </Col>
            </Row>
            <Row>
                <Col></Col>
                <Col md="auto"></Col>
                <Col xs lg="3">
                    <Pagination>
                        {
                            page > 0
                                ? <Pagination.Prev onClick={previousPage}/>
                                : <Pagination.Prev disabled />
                        }
                        <Pagination.Item>{page + 1}</Pagination.Item>
                        <Pagination.Item>{"/"}</Pagination.Item>
                        <Pagination.Item>{totalPage}</Pagination.Item>
                        {
                            page < totalPage - 1
                                ? <Pagination.Next onClick={nextPage}/>
                                : <Pagination.Next disabled />
                        }
                    </Pagination>
                </Col>
            </Row>

            <Modal
                show={show}
                onHide={handleConfirmDeleteModalClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header >
                    <Modal.Title>Xác nhận</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Bạn chắc chắn muốn xóa người dùng <span className="text-danger">{deleteUser.name}</span>.
                    <p className="text-warning"> Lưu ý: Thao tác này không thể hoàn tác </p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleConfirmDeleteModalClose}>
                        Đóng
                    </Button>
                    <Button variant="danger" onClick={() => handleDeleteUser()}>Xóa</Button>
                </Modal.Footer>
            </Modal>

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
                            title={deleteSuccess}
                        />
                    );
                })}
            </FlagGroup>
        </>
    )
}

export default UserList;